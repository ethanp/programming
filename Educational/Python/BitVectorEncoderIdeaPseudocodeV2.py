# if I use '-' and '_' I can use BASE 64, which saves a LOT of effort.
# so I'm going to do that. It's just ascii anyhow, so SQL and QR coders should have no issues with it...right??
class EBVEncoder:
    int size = 0
    NSStr encosion = ''

    lngArrLen = 10
    uint32_t *lngArr

    def __init__:
        lngArr = (uint64_t *)malloc(10*sizeof(uint64_t))

    def addBit(bitVal):
        size += 1
        lngArr[size/64] |= ((uint64_t)bitVal) << size%64

    def setBit(bitVal, loc):
        lngArr[loc/64] |= ((uint64_t)bitVal) << loc%64

    def getBit(loc):
        return (lngArr[loc/64] >> (loc%64)) & 1

    def encodeLngArr():
        # traverse 6 digits at a time, appending to the string as it goes
        int val = 0
        for i in range(size):
            val |= getBit(i)
            if i % 6 == 0:
                encosion += val2Char(val)
                val = 0

        return encosion

    def val2Char(val):
        if val < 10:
            return str(val)
        etc.

    # this MUST be given the size because it could be that the last value is 6 0's,
    # so it wouldn't know where to start/stop
    def decode(string, size):
        for i,ch in enumerate(string):
            val = char2Val(ch)
            for j in range(6):
                bitVal = (val >> j) & 1
                addBit(bitVal, i*6+j)
