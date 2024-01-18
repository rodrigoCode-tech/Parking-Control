package com.api.parkingcontrol.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;



@Entity
@Table(name = "TB_PARKING_SPOT")
public class ParkingSpotModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	    @Id
		@GeneratedValue(generator = "uuid2")
		@GenericGenerator(name = "uuid2", strategy = "uuid2")
		@Column(columnDefinition = "VARCHAR(36)")
	    @Type(type = "uuid-char")
		private UUID id;
	@NotBlank
    @Column(nullable = false, unique = true, length = 10)
    private String parkingSpotNumber;

	@NotBlank
    @Column(nullable = false, unique = true, length = 7)
    private String licensePlateCar;
    @NotBlank
    @Column(nullable = false, length = 70)
    private String brandCar;
    @NotBlank
    @Column(nullable = false, length = 70)
    private String modelCar;
    @NotBlank
    @Column(nullable = false, length = 70)
    private String colorCar;
    
    @Column(nullable = false)
    private LocalDateTime registrationDate;
    @NotBlank
    @Column(nullable = false, length = 130)
    private String responsibleName;
    @NotBlank
    @Column(nullable = false, length = 30)
    private String apartment;
    @NotBlank
    @Column(nullable = false, length = 30)
    private String block;
    
    public ParkingSpotModel() {
    }
    

	public ParkingSpotModel(UUID id, String parkingSpotNumber, String licensePlateCar, String brandCar, String modelCar,
			String colorCar, LocalDateTime registrationDate, String responsibleName, String apartment, String block) {
		super();
		this.id = id;
		this.parkingSpotNumber = parkingSpotNumber;
		this.licensePlateCar = licensePlateCar;
		this.brandCar = brandCar;
		this.modelCar = modelCar;
		this.colorCar = colorCar;
		this.registrationDate = registrationDate;
		this.responsibleName = responsibleName;
		this.apartment = apartment;
		this.block = block;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getParkingSpotNumber() {
		return parkingSpotNumber;
	}

	public void setParkingSpotNumber(String parkingSpotNumber) {
		this.parkingSpotNumber = parkingSpotNumber;
	}

	public String getLicensePlateCar() {
		return licensePlateCar;
	}

	public void setLicensePlateCar(String licensePlateCar) {
		this.licensePlateCar = licensePlateCar;
	}

	public String getBrandCar() {
		return brandCar;
	}

	public void setBrandCar(String brandCar) {
		this.brandCar = brandCar;
	}

	public String getModelCar() {
		return modelCar;
	}

	public void setModelCar(String modelCar) {
		this.modelCar = modelCar;
	}

	public String getColorCar() {
		return colorCar;
	}

	public void setColorCar(String colorCar) {
		this.colorCar = colorCar;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getResponsibleName() {
		return responsibleName;
	}

	public void setResponsibleName(String responsibleName) {
		this.responsibleName = responsibleName;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}
    
    
}
