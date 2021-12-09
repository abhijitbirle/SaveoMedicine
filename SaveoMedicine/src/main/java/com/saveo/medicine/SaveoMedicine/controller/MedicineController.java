package com.saveo.medicine.SaveoMedicine.controller;


import com.saveo.medicine.SaveoMedicine.exceptions.SaveoExceptionHandler;
import com.saveo.medicine.SaveoMedicine.model.PlaceOrderInfo;
import com.saveo.medicine.SaveoMedicine.model.TabletDataInfo;
import com.saveo.medicine.SaveoMedicine.service.SaveoMedicineProcessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.saveo.medicine.SaveoMedicine.model.MedicineConstant.FILE;

@RestController
@Log4j2
public class MedicineController {

    @Autowired
    private SaveoMedicineProcessor saveoMedicineProcessor;

    @PostMapping("/uploadCSV")
    private String uploadProductList(@RequestParam(FILE) MultipartFile productList) {
        if (!productList.isEmpty()) {
            saveoMedicineProcessor.updateMedicineList(productList);
            return "Got it";
        } else {
            throw new SaveoExceptionHandler("400", "File Not found");
        }
    }

    @GetMapping("/searchMedicine/{tablet}")
    private HashSet<TabletDataInfo> searchTablet(@PathVariable("tablet") String tabletName) {
        return new HashSet<>(saveoMedicineProcessor.getTabletList(tabletName));
    }

    @GetMapping("/getMedicineDetails/{c_unique_id}")
    private TabletDataInfo getMedicineDetails(@PathVariable("c_unique_id") String cUniqueId) {
        return saveoMedicineProcessor.getMedicineDetails(cUniqueId);
    }

    @PostMapping("/placeOrder")
    private String placeOrder(@RequestBody List<PlaceOrderInfo> orderInfoData) {
        saveoMedicineProcessor.placeTabletOrder(orderInfoData);
        return UUID.randomUUID().toString();
    }
}