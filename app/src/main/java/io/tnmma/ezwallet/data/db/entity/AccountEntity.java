package io.tnmma.ezwallet.data.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.ResultCode;
import io.tnmma.ezwallet.data.model.ColorItem;
import io.tnmma.ezwallet.util.CommonUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity(
        tableName = "ivy_account",
        indices = {
            @Index(
                    value = {"id"},
                    unique = true)
        })
public class AccountEntity implements Serializable {

    @NonNull @PrimaryKey private UUID id;

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
            return ResultCode.Account.EMPTY_ID;
        }

        if (CommonUtil.isNullOrEmpty(name)) {
            return ResultCode.Account.EMPTY_NAME;
        }

        if (!isColorValid()) {
            return ResultCode.Account.INVALID_COLOR;
        }

        if (createdTime == null) {
            return ResultCode.Account.EMPTY_CREATED_TIME;
        }

        return ResultCode.Account.VALID;
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
