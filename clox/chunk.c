#include <stdlib.h>

#include "chunk.h"
#include "memory.h"

void initChunk(Chunk *chunk)
{
    chunk->count = 0;
    chunk->capacity = 0;
    chunk->code = NULL;
    chunk->lines = NULL;
    chunk->lines_opcode_counts = NULL;
    chunk->lines_capacity = 0;
    chunk->lines_count = 0;
    chunk->last_line = 0;
    initValueArray(&chunk->constants);
}

void freeChunk(Chunk *chunk)
{
    FREE_ARRAY(uint8_t, chunk->code, chunk->capacity);
    freeValueArray(&chunk->constants);
    FREE_ARRAY(int, chunk->lines, chunk->lines_capacity);
    FREE_ARRAY(int, chunk->lines_opcode_counts, chunk->lines_capacity);
    initChunk(chunk);
}

void writeChunk(Chunk *chunk, uint8_t byte, int line)
{
    // we modify Chunk so that:
    //
    // `lines` maps 1:1 to line numbers rather than to offsets
    // `lines_opcode_counts` records how many opcodes we have at that line number
    // we allow line numbers to jump back and forth - they don't need to be increasing
    // to fetch a line number, we start at lines[i] where i = 0, and we only increment
    // i when we've seen lines_opcode_counts[i] number of opcodes.

    if (chunk->capacity < chunk->count + 1)
    {
        int oldCapacity = chunk->capacity;
        chunk->capacity = GROW_CAPACITY(oldCapacity);
        chunk->code = GROW_ARRAY(uint8_t, chunk->code, oldCapacity, chunk->capacity);
    }

    if (chunk->last_line == line)
    {
        chunk->lines_opcode_counts++;
    }
    else
    {
        // grow lines dlist if needed
        if (chunk->lines_capacity < chunk->lines_count + 1)
        {
            int oldCapacity = chunk->lines_capacity;
            chunk->lines_capacity = GROW_CAPACITY(oldCapacity);
            chunk->lines = GROW_ARRAY(int, chunk->lines, oldCapacity, chunk->lines_capacity);
            chunk->lines_opcode_counts = GROW_ARRAY(int, chunk->lines_opcode_counts, oldCapacity, chunk->lines_capacity);
        }
        // record the line
        chunk->lines[chunk->lines_count] = line;
        chunk->lines_opcode_counts[chunk->lines_count]++;
        chunk->lines_count++;
    }

    chunk->code[chunk->count] = byte;
    chunk->lines[chunk->count] = line;
    chunk->count++;
}

int addConstant(Chunk *chunk, Value value)
{
    writeValueArray(&chunk->constants, value);
    return chunk->constants.count - 1;
}

int calcChunkLine(Chunk *chunk, int offset)
{
    for (int i = 0; i < chunk->lines_count; i++)
    {
        offset -= chunk->lines_opcode_counts[i];
        if (offset < 0)
        {
            return chunk->lines[i];
        }
    }
    return -1;
}
