package main.nbt;

import main.exception.NBTException;

import java.util.ArrayList;

public class ListTag extends ArrayList<Object> {

    public short type;

    public ListTag(short type) {
        this.type = type;
    }


    @Override//克隆新的列表标签对象
    public ListTag clone() {
        ListTag tag = new ListTag(type);
        for (int i = 0; i < super.size(); i++) {
            tag.addTag(TagType.cloneTag(super.get(i)));
        }
        return tag;
    }

    /**
     * 获取列表标签
     *
     * @param index 索引
     * @return 列表标签
     */
    public ListTag getListTag(int index) {
        return (ListTag) super.get(index);
    }

    /**
     * 获取复合标签
     *
     * @param index 索引
     * @return 复合标签
     */
    public CompoundTag getCompoundTag(int index) {
        return (CompoundTag) super.get(index);
    }

    /**
     * 获取标签对象
     *
     * @param index 索引
     * @return 标签对象
     */
    public Object getTag(int index) {
        return super.get(index);
    }

    /**
     * 增加一个标签
     *
     * @param o 欲增加的标签对象
     * @return 自身
     * @throws NBTException
     */
    public ListTag addTag(Object o) throws NBTException {
        if (type == TagType.Object2TagType(o)) {
            super.add(o);
            return this;
        } else {
            throw new NBTException("List类型错误 不能为:" + o.getClass().getName());
        }

    }


}
