#ifndef clox_chunk_h
#define clox_chunk_h

#include "common.h"
#include "value.h"

typedef enum
{
    OP_CONSTANT,
    OP_RETURN,
} OpCode;

typedef struct
{
    int count;
    int capacity;
    uint8_t *code;
    int *lines;
    int *lines_opcode_counts;
    int lines_capacity;
    int lines_count;
    int last_line;
    ValueArray constants;
} Chunk;

void initChunk(Chunk *chunk);
void freeChunk(Chunk *chunk);
void writeChunk(Chunk *chunk, uint8_t byte, int line);
int addConstant(Chunk *chunk, Value value);
int calcChunkLine(Chunk *chunk, int offset);

#endif
