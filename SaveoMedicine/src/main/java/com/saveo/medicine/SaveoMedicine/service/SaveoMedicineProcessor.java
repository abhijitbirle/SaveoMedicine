package com.saveo.medicine.SaveoMedicine.service;

import com.saveo.medicine.SaveoMedicine.exceptions.SaveoExceptionHandler;
import com.saveo.medicine.SaveoMedicine.model.PlaceOrderInfo;
import com.saveo.medicine.SaveoMedicine.model.TabletDataInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.saveo.medicine.SaveoMedicine.model.MedicineConstant.C_NAME;
import static com.saveo.medicine.SaveoMedicine.model.MedicineConstant.C_UNIQUE_CODE;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Log4j2
public class SaveoMedicineProcessor {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateMedicineList(MultipartFile productList) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(productList.getInputStream(),
                UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<TabletDataInfo> tabletDataInfos = new ArrayList<>();
            if (Objects.nonNull(csvParser.getRecords())) {
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                for (CSVRecord csvRecord : csvRecords) {
                    TabletDataInfo tabletDataInfo = new TabletDataInfo();
                    tabletDataInfo.setC_name(csvRecord.get(C_NAME));
                    tabletDataInfo.setC_batch_no(csvRecord.get("c_batch_no"));
                    tabletDataInfo.setC_manufacturer(csvRecord.get("c_manufacturer"));
                    tabletDataInfo.setC_packaging(csvRecord.get("c_packaging"));
                    tabletDataInfo.setC_schemes(csvRecord.get("c_schemes"));
                    tabletDataInfo.setD_expiry_date(csvRecord.get("d_expiry_date"));
                    tabletDataInfo.setC_schemes(csvRecord.get("c_schemes"));
                    tabletDataInfo.setC_unique_code(csvRecord.get(C_UNIQUE_CODE));
                    tabletDataInfo.setHsn_code(Integer.parseInt(csvRecord.get("hsn_code")));
                    tabletDataInfo.setN_balance_qty(Integer.parseInt(csvRecord.get("n_balance_qty")));
                    tabletDataInfo.setN_mrp(Double.parseDouble(csvRecord.get("n_mrp")));
                    tabletDataInfos.add(tabletDataInfo);
                }
                tabletDataInfos.forEach(System.out::println);
                mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TabletDataInfo.class)
                        .insert(tabletDataInfos).execute();
            }
        } catch (IOException e) {
            throw new SaveoExceptionHandler("400", "Unable toi parse the file");
        }
    }

    public List<TabletDataInfo> getTabletList(String tablet) {
        Query query = new Query();
        query.addCriteria(Criteria.where(C_NAME).regex(tablet));
        query.fields().include(C_NAME).project(MongoExpression.create(C_NAME));
        return mongoTemplate.find(query, TabletDataInfo.class);
    }

    public TabletDataInfo getMedicineDetails(String cUniqueId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(C_UNIQUE_CODE).regex(cUniqueId));
        return mongoTemplate.findOne(query, TabletDataInfo.class);
    }

    public void placeTabletOrder(List<PlaceOrderInfo> orderInfoData) {
        Query query = new Query();
        List<String> tableNameList = orderInfoData.stream().map(PlaceOrderInfo::getC_name).collect(Collectors.toList());
        List<String> tableUniqueCodeList = orderInfoData.stream().map(PlaceOrderInfo::getC_unique_code)
                .collect(Collectors.toList());
        query.addCriteria(Criteria.where(C_UNIQUE_CODE).in(tableUniqueCodeList))
                .addCriteria(Criteria.where(C_NAME).in(tableNameList));
        List<TabletDataInfo> tabletDataInfos = mongoTemplate.find(query, TabletDataInfo.class);
        for (PlaceOrderInfo p : orderInfoData) {
            for (TabletDataInfo t : tabletDataInfos) {
                if (p.getC_name().equals(t.getC_name()) && p.getC_unique_code().equals(t.getC_unique_code())) {
                    t.setN_balance_qty(t.getN_balance_qty() - p.getQuantity());
                }
            }
        }
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TabletDataInfo.class)
                .insert(tabletDataInfos).execute();
    }
}
