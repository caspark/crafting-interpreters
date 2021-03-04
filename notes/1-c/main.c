#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// A single link of a doubly-linked list; all fields are public for reading, but should not be
// written to directly.
// The memory pointed to by `s` is always owned by the linked list and should not be freed manually.
struct node
{
   struct node *next;
   struct node *prev;
   char *s;
} node;

// Implementation detail
char *list__strdup(char *s)
{
   assert(s != NULL);
   char *sCopy = strdup(s);
   assert(sCopy != NULL);
   return sCopy;
}

// Implementation detail
void list__free_node(struct node *link)
{
   free(link->s);
   link->s = NULL;
   link->next = NULL;
   link->prev = NULL;
   free(link);
}

// API: create a new list, initialized with 1 element that points to a copy of the given String.
struct node *list_init(char *s)
{
   char *sCopy = list__strdup(s);

   struct node *new = malloc(sizeof(node));
   assert(new != NULL);
   new->next = NULL;
   new->prev = NULL;
   new->s = sCopy;
   return new;
}

// API: append a copy of the given string as the next item after the given link.
struct node *list_append_after(struct node *link, char *s)
{
   assert(link != NULL);

   struct node *new = list_init(s);
   new->prev = link;

   if (link->next != NULL)
   {
      link->next->prev = new;
      new->next = link->next;
   }
   link->next = new;

   return new;
}

// API: prepend a copy of the given string as the previous item before the given link
struct node *list_prepend_before(struct node *link, char *s)
{
   assert(link != NULL);

   struct node *new = list_init(s);
   new->next = link;

   if (link->prev != NULL)
   {
      link->prev->next = new;
      new->prev = link->prev;
   }
   link->prev = new;

   return new;
}

// API: find the first element in the list
struct node *list_head(struct node *link)
{
   assert(link != NULL);
   while (link->prev != NULL)
   {
      link = link->prev;
   }
   return link;
}

// API: find the last element in the list
struct node *list_last(struct node *link)
{
   assert(link != NULL);
   while (link->next != NULL)
   {
      link = link->next;
   }
   return link;
}

// API: free the entire list (including all nodes it links to) and all strings pointed to by it.
void list_free(struct node *link)
{
   link = list_head(link);

   do
   {
      struct node *old = link;
      link = link->next;

      list__free_node(old);
   } while (link != NULL);
}

// API: remove and free the given link from the list (but keep the rest of the list intact)
void list_free_only(struct node *link)
{
   assert(link != NULL);

   if (link->prev != NULL)
   {
      link->prev->next = link->next;
   }
   if (link->next != NULL)
   {
      link->next->prev = link->prev;
   }

   list__free_node(link);
}

// API: output a debug representation of the list to stdout (the format of the output is not API)
void list_debug_dump(struct node *link)
{
   link = list_head(link);

   int i = 0;
   do
   {
      printf("Link %d: %s\n", i, link->s);

      i++;
      link = link->next;
   } while (link != NULL);
}

// Given any link in a list, checks that:
// - the first element of the list is "item 0"
// - the last element is "item N" where N = length of list
// - all other items in the list are in between
// - next and prev pointers are set appropriately
// - string pointers are non-NULL
void check_list_integrity_and_contents_are_ascending(struct node *original)
{
   struct node *head = list_head(original);

   size_t expected_item_str_size = strlen("item ") + 12 + 1; // 12 chars fits any int, +1 for \0

   int i = 0;
   struct node *link = head;
   do
   {
      char *expected_item_str = (char *)malloc(expected_item_str_size * sizeof(char));
      assert(expected_item_str != NULL);
      sprintf(expected_item_str, "item %d", i);
      if (link->s == NULL)
      {
         printf("!!! Error at position %d: expected '%s' but got string pointer at link is NULL!\n", i, expected_item_str);
         assert(0);
      }
      if (strcmp(expected_item_str, link->s) != 0)
      {
         printf("!!! Error at position %d: expected '%s' but got '%s'; list contents:\n", i, expected_item_str, link->s);
         list_debug_dump(head);
         printf("!!! end list dump, see error above\n");
         assert(0);
      }
      free(expected_item_str);

      i++;

      struct node *old = link;
      link = link->next;
      if (link != NULL && link->prev != old)
      {
         printf("!!! Error near position %d: node at %d does not point back to node at %d; list contents:\n", i, i + 1, i);
         list_debug_dump(head);
         printf("!!! end list dump, see error above\n");
         assert(0);
      }
   } while (link != NULL);
}

int main()
{
   printf("Hello, World!\n");

   printf("Test 1 begin - initialization\n");
   {
      struct node *list = list_init("item 0");

      check_list_integrity_and_contents_are_ascending(list);
      list_free(list);
   }

   printf("Test 2 begin - appending elements to end of list\n");
   {
      struct node *node0 = list_init("item 0");
      struct node *node1 = list_append_after(node0, "item 1");
      struct node *node2 = list_append_after(node1, "item 2");

      check_list_integrity_and_contents_are_ascending(node2);
      list_free(node2);
   }

   printf("Test 2 begin - find first and last item of list\n");
   {
      struct node *node0 = list_init("item 0");
      struct node *node1 = list_append_after(node0, "item 1");
      struct node *node2 = list_append_after(node1, "item 2");

      check_list_integrity_and_contents_are_ascending(node2);
      assert(strcmp(list_head(node2)->s, "item 0") == 0);
      assert(strcmp(list_last(node0)->s, "item 2") == 0);
      list_free(node2);
   }

   printf("Test 3 begin - appending elements in middle of list\n");
   {
      struct node *node0 = list_init("item 0");
      struct node *node2 = list_append_after(node0, "item 2");
      list_append_after(node0, "item 1");

      check_list_integrity_and_contents_are_ascending(node2);
      list_free(node2);
   }

   printf("Test 4 begin - prepending elements to start of list\n");
   {
      struct node *node2 = list_init("item 2");
      struct node *node1 = list_prepend_before(node2, "item 1");
      struct node *node0 = list_prepend_before(node1, "item 0");

      check_list_integrity_and_contents_are_ascending(node0);
      list_free(node0);
   }

   printf("Test 5 begin - prepending elements to in middle of list\n");
   {
      struct node *node2 = list_init("item 2");
      struct node *node0 = list_prepend_before(node2, "item 0");
      list_prepend_before(node2, "item 1");

      check_list_integrity_and_contents_are_ascending(node0);
      list_free(node0);
   }

   printf("Test 6 begin - deleting element from start of list\n");
   {
      struct node *nodeTemp = list_init("item -1");
      struct node *node0 = list_append_after(nodeTemp, "item 0");
      struct node *node1 = list_append_after(node0, "item 1");

      list_free_only(nodeTemp);

      check_list_integrity_and_contents_are_ascending(node1);
      list_free(node1);
   }

   printf("Test 7 begin - deleting element from middle of list\n");
   {
      struct node *node0 = list_init("item 0");
      struct node *nodeTemp = list_append_after(node0, "item 0.5");
      struct node *node1 = list_append_after(nodeTemp, "item 1");

      list_free_only(nodeTemp);

      check_list_integrity_and_contents_are_ascending(node1);
      list_free(node1);
   }

   printf("Test 8 begin - deleting element from end of list\n");
   {
      struct node *node0 = list_init("item 0");
      struct node *node1 = list_append_after(node0, "item 1");
      struct node *nodeTemp = list_append_after(node1, "item 2");

      list_free_only(nodeTemp);

      check_list_integrity_and_contents_are_ascending(node1);
      list_free(node1);
   }

   return 0;
}
