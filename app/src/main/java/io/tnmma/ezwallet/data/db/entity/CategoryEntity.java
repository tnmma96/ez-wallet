package io.tnmma.ezwallet.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import io.tnmma.ezwallet.base.constant.CategoryType;
import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.data.model.ColorItem;
import io.tnmma.ezwallet.util.CommonUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity(
        tableName = "ivy_category",
        indices = {
            @Index(
                    value = {"id"},
                    unique = true)
        })
public class CategoryEntity implements Serializable {

    @NonNull @PrimaryKey private UUID id;

    @NonNull
    @ColumnInfo(name = "category_type")
    private CategoryType categoryType;

    @NonNull private String name;

    private int color;

    private String icon;

    @NonNull
    @ColumnInfo(name = "created_time")
    private Instant createdTime;

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public CategoryType getCategoryType() {
        return categoryType;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }

    @NonNull
    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public void setCategoryType(@NonNull CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setCreatedTime(@NonNull Instant createdTime) {
        this.createdTime = createdTime;
    }

    public int validate() {
        if (id == null) {
            return ResultCode.Category.EMPTY_ID;
        }

        if (CommonUtil.isNullOrEmpty(name)) {
            return ResultCode.Category.EMPTY_NAME;
        }

        if (categoryType == null) {
            return ResultCode.Category.EMPTY_CATEGORY_TYPE;
        }

        if (!isColorValid()) {
            return ResultCode.Category.INVALID_COLOR;
        }

        if (createdTime == null) {
            return ResultCode.Category.EMPTY_CREATED_TIME;
        }

        return ResultCode.Category.VALID;
    }

    private boolean isColorValid() {
        for (ColorItem item : ConstantArrays.getColorList()) {
            if (item.getColor() == color) {
                return true;
            }
        }
        return false;
    }
}
