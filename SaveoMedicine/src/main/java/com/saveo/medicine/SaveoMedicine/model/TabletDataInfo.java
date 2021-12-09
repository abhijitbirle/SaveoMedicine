package com.saveo.medicine.SaveoMedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Document("medicines")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TabletDataInfo {

    @NonNull
    private String c_name;

    @NonNull
    private String c_unique_code;

    @NonNull
    private String c_batch_no;

    @NonNull
    private String d_expiry_date;

    @NonNull
    private Integer n_balance_qty;

    @NonNull
    private String c_packaging;

    @NonNull
    private String c_schemes;

    @NonNull
    private Double n_mrp;

    @NonNull
    private String c_manufacturer;

    @NonNull
    private Integer hsn_code;

    public TabletDataInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabletDataInfo)) return false;
        TabletDataInfo that = (TabletDataInfo) o;
        return getC_name().equals(that.getC_name()) && getC_unique_code().equals(that.getC_unique_code()) &&
                getC_batch_no().equals(that.getC_batch_no());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getC_name(), getC_unique_code(), getC_batch_no());
    }
}
