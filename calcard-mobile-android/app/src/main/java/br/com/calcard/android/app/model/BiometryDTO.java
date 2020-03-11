package br.com.calcard.android.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BiometryDTO {

    @SerializedName("deviceDTO")
    @Expose
    DeviceDTO deviceDTO;

    @SerializedName("photo")
    @Expose
    String photo;

    public BiometryDTO(DeviceDTO deviceDTO, String photo) {
        this.deviceDTO = deviceDTO;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public DeviceDTO getDeviceDTO() {
        return deviceDTO;
    }

    public void setDeviceDTO(DeviceDTO deviceDTO) {
        this.deviceDTO = deviceDTO;
    }
}
