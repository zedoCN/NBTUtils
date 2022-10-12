package main.nbt;

import java.util.Objects;

public final class TagType {
    public static final short TAG_End = 0;
    public static final short TAG_Byte = 1;
    public static final short TAG_Short = 2;
    public static final short TAG_Int = 3;
    public static final short TAG_Long = 4;
    public static final short TAG_Float = 5;
    public static final short TAG_Double = 6;
    public static final short TAG_Byte_Array = 7;
    public static final short TAG_String = 8;
    public static final short TAG_List = 9;
    public static final short TAG_Compound = 10;
    public static final short TAG_Int_Array = 11;
    public static final short TAG_Long_Array = 12;

    /**
     * 对象到标签类型
     *
     * @param o 对象
     * @return 标签类型 没有返回-1
     */
    public static short Object2TagType(Object o) {
        if (o == null)
            return -1;
        Class oc = o.getClass();
        if (oc.equals(Byte.class)) {
            return TAG_Byte;
        } else if (oc.equals(Short.class)) {
            return TAG_Short;
        } else if (oc.equals(Integer.class)) {
            return TAG_Int;
        } else if (oc.equals(Long.class)) {
            return TAG_Long;
        } else if (oc.equals(Float.class)) {
            return TAG_Float;
        } else if (oc.equals(Double.class)) {
            return TAG_Double;
        } else if (oc.equals(byte[].class)) {
            return TAG_Byte_Array;
        } else if (oc.equals(String.class)) {
            return TAG_String;
        } else if (oc.equals(ListTag.class)) {
            return TAG_List;
        } else if (oc.equals(CompoundTag.class)) {
            return TAG_Compound;
        } else if (oc.equals(int[].class)) {
            return TAG_Int_Array;
        } else if (oc.equals(long[].class)) {
            return TAG_Long_Array;
        } else {
            return -1;
        }
    }

    /**
     * 标签类型取标签类型名
     *
     * @param type 标签类型
     * @return 标签类型名
     */
    public static String TagType2Str(short type) {
        return switch (type) {
            case TAG_End -> "TAG_End";
            case TAG_Byte -> "TAG_Byte";
            case TAG_Short -> "TAG_Short";
            case TAG_Int -> "TAG_Int";
            case TAG_Long -> "TAG_LONG";
            case TAG_Float -> "TAG_Float";
            case TAG_Double -> "TAG_Double";
            case TAG_Byte_Array -> "TAG_Byte_Array";
            case TAG_String -> "TAG_String";
            case TAG_List -> "TAG_List";
            case TAG_Compound -> "TAG_Compound";
            case TAG_Int_Array -> "TAG_Int_Array";
            case TAG_Long_Array -> "TAG_Long_Array";
            default -> null;
        };
    }

    /**
     * 复制标签
     *
     * @param object 欲复制的标签对象
     * @return 新的标签对象
     */
    public static Object cloneTag(Object object) {
        return switch (TagType.Object2TagType(object)) {
            case TAG_Byte_Array -> ((byte[]) object).clone();
            case TAG_List -> ((ListTag) object).clone();
            case TAG_Compound -> ((CompoundTag) object).clone();
            case TAG_Int_Array -> ((int[]) object).clone();
            case TAG_Long_Array -> ((long[]) object).clone();
            default -> object;
        };
    }
}
