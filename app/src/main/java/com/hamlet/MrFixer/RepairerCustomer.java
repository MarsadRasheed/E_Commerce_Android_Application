package com.hamlet.MrFixer;

public class RepairerCustomer {
    private String name;
    private String contact;
    private String cnic;
    private String deviceType;
    private String deviceName;
    private String deviceModel;
    private String deviceProblem;
    private String address;

    public RepairerCustomer() {
    }

    public RepairerCustomer(String name, String contact, String cnic, String deviceType, String deviceName, String deviceModel, String deviceProblem, String address) {
        this.name = name;
        this.contact = contact;
        this.cnic = cnic;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.deviceModel = deviceModel;
        this.deviceProblem = deviceProblem;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceProblem() {
        return deviceProblem;
    }

    public void setDeviceProblem(String deviceProblem) {
        this.deviceProblem = deviceProblem;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "RepairerCustomer{" +
                "name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", cnic='" + cnic + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceProblem='" + deviceProblem + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
