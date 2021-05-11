#include "common.h"
#include "chunk.h"
#include "debug.h"

int main(int argc, const char *argv[])
{
    Chunk chunk;
    initChunk(&chunk);

    // int constant = addConstant(&chunk, 1.2);
    // writeChunk(&chunk, OP_CONSTANT, 123);
    // writeChunk(&chunk, constant, 123);
    for (int i = 0; i < 257; i++)
    {
        writeConstant(&chunk, (float)i * 10, i);
    }

    writeChunk(&chunk, OP_RETURN, 1000);

    disassembleChunk(&chunk, "test chunk");
    freeChunk(&chunk);

    return 0;
}
