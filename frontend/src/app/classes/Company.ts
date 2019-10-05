export class Company {
    address:Address;     
    branches:Branch[];
    category: string;
    companyWebsiteUrl: string;
    description: string;
    name: string;
    nip: string;
    regon: string;
}

export class Branch {
    address: Address;       
    geoX: string;
    geoY: string;
    name: string;
}
export class Address {
    apartmentNo: string;
    buildingNo: string;
    city: string;
    street: string;
    voivodeship: string;
}