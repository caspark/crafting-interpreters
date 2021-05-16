#include "common.h"
#include "chunk.h"
#include "debug.h"
#include "vm.h"

int main(int argc, const char *argv[])
{
    initVM();

    Chunk chunk;
    initChunk(&chunk);

    for (int i = 0; i <= 200; i++)
    {
        int constant = addConstant(&chunk, (float)i);
        writeChunk(&chunk, OP_CONSTANT, i);
        writeChunk(&chunk, constant, 123);
    }

    writeChunk(&chunk, OP_RETURN, 123);

    interpret(&chunk);
    freeVM();
    freeChunk(&chunk);

    return 0;
}
