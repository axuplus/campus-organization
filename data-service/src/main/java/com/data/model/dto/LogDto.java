package com.data.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class LogDto implements java.io.Serializable {
    private Long id;
    private String name;
    private String userId;
    private String phone;
    private String idNumber;
    private String equipment;
    private String equipmentName;
    private String photoPath;
    private String time;
    private Date createdAt;

//    public LogDto(String name, String userId, String phone, String idNumber, String equipment, String equipmentName, String photoPath, String time, Date createdAt) {
//        this.name = name;
//        this.userId = userId;
//        this.phone = phone;
//        this.idNumber = idNumber;
//        this.equipment = equipment;
//        this.equipmentName = equipmentName;
//        this.photoPath = photoPath;
//        this.time = time;
//        this.createdAt = createdAt;
//    }
//
//    public static LogDto.Builder newBuilder() {
//        return new LogDto.Builder();
//    }
//
//    public static class Builder {
//        private String name;
//        private String userId;
//        private String phone;
//        private String idNumber;
//        private String equipment;
//        private String equipmentName;
//        private String photoPath;
//        private String time;
//        private Date createdAt;
//
//
//        public LogDto.Builder setName(String name) {
//            this.name = name;
//            return this;
//        }
//
//
//        public LogDto.Builder setUserId(String userId) {
//            this.userId = userId;
//            return this;
//        }
//
//        public LogDto.Builder setIdNumber(String idNumber) {
//            this.idNumber = idNumber;
//            return this;
//        }
//
//        public LogDto.Builder setEquipment(String equipment) {
//            this.equipment = equipment;
//            return this;
//        }
//
//        public LogDto.Builder setEquipmentName(String equipmentName) {
//            this.equipmentName = equipmentName;
//            return this;
//        }
//
//        public LogDto.Builder setPhone(String phone) {
//            this.phone = phone;
//            return this;
//        }
//
//        public LogDto.Builder setPhotoPath(String photoPath) {
//            this.photoPath = photoPath;
//            return this;
//        }
//
//        public LogDto.Builder setCreatedAt(Date createdAt) {
//            this.createdAt = createdAt;
//            return this;
//        }
//
//        public LogDto.Builder setTime(String time) {
//            this.time = time;
//            return this;
//        }
//
//
//        public LogDto build() {
//            return new LogDto(this.name, this.userId, this.idNumber, this.equipment, this.equipmentName, this.phone, this.photoPath, this.time, this.createdAt);
//        }
//    }
}
