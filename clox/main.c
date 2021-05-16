#include "common.h"
#include "chunk.h"
#include "debug.h"
#include "vm.h"

int main(int argc, const char *argv[])
{
    initVM();

    Chunk chunk;
    initChunk(&chunk);

    int constant = addConstant(&chunk, 1.0);
    writeChunk(&chunk, OP_CONSTANT, 1);
    writeChunk(&chunk, constant, 1);

    for (int i = 0; i < 100000000; i++)
    {
        writeChunk(&chunk, OP_NEGATE, i);
    }

    writeChunk(&chunk, OP_RETURN, 99999);

    interpret(&chunk);
    freeVM();
    freeChunk(&chunk);

    return 0;
}
