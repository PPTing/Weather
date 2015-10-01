package me.ppting.weather;

/**
 * Created by PPTing on 15/10/1.
 */
public class CityInfo
{
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public CityInfoResult getResult() {
        return result;
    }

    public void setResult(CityInfoResult result) {
        this.result = result;
    }

    private CityInfoResult  result;
    public class CityInfoResult
    {
        public AddressComponent getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponent addressComponent) {
            this.addressComponent = addressComponent;
        }

        private AddressComponent addressComponent;
        public class AddressComponent
        {
            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            private String city;

        }
    }
}
