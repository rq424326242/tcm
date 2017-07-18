package tcm.com.gistone.database.mapper;

import tcm.com.gistone.database.entity.Special;
import tcm.com.gistone.database.entity.SpecialBook;

public interface SpecialMapper {
    int deleteByPrimaryKey(Long specialId);

    int insert(SpecialBook sb);

    int insertSelective(Special record);

    Special selectByPrimaryKey(Long specialId);

    int updateByPrimaryKeySelective(Special record);

    int updateByPrimaryKey(Special record);

}