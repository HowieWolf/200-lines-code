package data_structure;

/**
 * 布尔数组
 * 使用一个 bit 表示一个布尔值，优化内存
 */
public class BooleanArray {
    private static final int BIT = 8;
    private final int mLen;
    private final byte[] mData;

    public BooleanArray(int len) {
        mLen = len;
        int count = 0;
        if (len <= BIT) {
            count = 1;
        } else if (len % BIT == 0) {
            count = len / BIT;
        } else {
            count = len / BIT + 1;
        }
        mData = new byte[count];
    }

    public boolean getValue(int index) {
        if (index < 0 || index >= mLen) {
            return false;
        }
        return (mData[getRow(index)] & (1 << getOffset(index))) != 0;
    }

    public void setValue(int index, boolean value) {
        if (index < 0 || index >= mLen) {
            return;
        }
        if (value) {
            mData[getRow(index)] |= (1 << getOffset(index));
        } else {
            mData[getRow(index)] &= ~(1 << getOffset(index));
        }
    }

    public int countValue(boolean value) {
        int countTrue = countTrue();
        return value ? countTrue : mLen - countTrue;
    }

    public int countTrue() {
        int count = 0;
        for (byte b : mData) {
            // 统计 byte 中 1 的个数
            // https://leetcode-cn.com/problems/number-of-1-bits/solution/wei-1de-ge-shu-by-leetcode-solution-jnwf/
            while (b != 0) {
                b &= (b - 1);
                count++;
            }
        }
        return count;
    }

    public int getLength() {
        return mLen;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; i < mLen; i++) {
            builder.append(getValue(i) ? "1" : "0");
            if (i < mLen - 1) {
                builder.append(',');
            }
        }
        builder.append(']');
        return builder.toString();
    }

    private int getRow(int index) {
        return index / BIT;
    }

    private int getOffset(int index) {
        return index % BIT;
    }
}
