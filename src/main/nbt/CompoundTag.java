package main.nbt;

import java.util.*;

import static main.nbt.TagType.*;

public class CompoundTag extends LinkedHashMap<String, Object> {


    public CompoundTag() {
    }

    @Override//克隆新的复合标签对象
    public CompoundTag clone() {
        CompoundTag clone = (CompoundTag) super.clone();
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<String, Object> entry : super.entrySet()) {
            tag.put(entry.getKey(), cloneTag(entry.getValue()));
        }
        return tag;
    }

    public ListTag getListTag(String name) {
        return (ListTag) (Object) super.get(name);
    }

    /**
     * 获取复合标签
     *
     * @param name 标签名
     * @return 复合标签 没有返回null
     */
    public CompoundTag getCompoundTag(String name) {
        return (CompoundTag) super.get(name);
    }

    /**
     * 获取标签
     *
     * @param name 标签名
     * @return 标签 没有返回null
     */
    public Object getTag(String name) {
        return super.get(name);
    }

    /**
     * 获取标签的类型
     *
     * @param name 标签名
     * @return 标签类型 没有返回-1
     */
    public short getTagType(String name) {
        return TagType.Object2TagType(super.get(name));
    }

    /**
     * 设置列表标签
     *
     * @param name    标签名
     * @param listTag 要设置的列表标签
     * @return 自身
     */
    public CompoundTag setListTag(String name, ListTag listTag) {
        super.put(name, listTag);
        return this;
    }

    /**
     * 设置复合标签
     *
     * @param name        标签名
     * @param compoundTag 要设置的复合标签
     * @return 自身
     */
    public CompoundTag setCompoundTag(String name, CompoundTag compoundTag) {
        super.put(name, compoundTag);
        return this;
    }

    /**
     * 设置新的复合标签
     *
     * @param name 标签名
     * @return 新的复合标签
     */
    public CompoundTag setCompoundTag(String name) {
        CompoundTag newCompoundTag = new CompoundTag();
        super.put(name, newCompoundTag);
        return newCompoundTag;
    }

    /**
     * 设置标签
     *
     * @param name 标签名
     * @param data 标签对象
     * @return 自身
     */
    public CompoundTag setTag(String name, Object data) {
        super.put(name, data);
        return this;
    }


}
