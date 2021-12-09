package com.saveo.medicine.SaveoMedicine.repository;

import com.saveo.medicine.SaveoMedicine.model.TabletDataInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaveoMedicineRepository extends MongoRepository<TabletDataInfo, String> {
}
