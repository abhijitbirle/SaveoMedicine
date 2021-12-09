package com.saveo.medicine.SaveoMedicine.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceOrderInfo {

    @NonNull
    private String c_name;

    @NonNull
    private String c_unique_code;

    @NonNull
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceOrderInfo)) return false;
        PlaceOrderInfo that = (PlaceOrderInfo) o;
        return getC_name().equals(that.getC_name()) && getC_unique_code().equals(that.getC_unique_code()) &&
                getQuantity().equals(that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getC_name(), getC_unique_code(), getQuantity());
    }
}
