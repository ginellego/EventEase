package comp3350.a15.eventease.objects;

import java.util.ArrayList;

public class VendorListInEvent {
    private final ArrayList<Vendor> vendorList;

    public VendorListInEvent (){
        this.vendorList = new ArrayList<Vendor>();
    }

    public Vendor getVendor(Vendor vendor){
        if(vendorList.contains(vendor)){
            for(int i = 0; i < vendorList.size(); i++){
                if(vendorList.get(i) == vendor){
                    return vendorList.get(i);
                }
            }
        }
        return null;
    }

    public Vendor getVendorAtPosition(int position){
        if(vendorList.get(position) != null){
            return vendorList.get(position);
        }
        return null;
    }

    public void addVendor(Vendor vendor){
        if(!this.vendorList.contains(vendor)){
            this.vendorList.add(vendor);
        }
    }

    public void removeVendor(Vendor vendor){
        this.vendorList.remove(vendor);
    }


}
