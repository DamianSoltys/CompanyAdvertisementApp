export class Company {
    logo:FormData;
    address:Address;     
    branches:Branch[];
    category: string;
    companyWebsiteUrl: string;
    description: string;
    name: string;
    nip: string;
    regon: string;
}

export class GetCompany {
    address:Address;     
    branchesIDs:number[];
    category: string;
    companyId:number;
    companyWebsiteUrl: string;
    description: string;
    companyName: string;
    nip: string;
    regon: string;
    registererFullname:string;
    logoKey?:string;
    logoURL?:string;
    logo?:any;
    hasLogoAdded?:boolean;
}

export class Branch {
    address: Address;       
    geoX: string;
    geoY: string;
    name: string;
    branchId?:number;
    companyId?:number;
    logoKey?:string;
    logoURL?:string;
    logoPath?:string;
    logo?:any;
    hasLogoAdded?:boolean;
}

export class Address {
    apartmentNo: string;
    buildingNo: string;
    city: string;
    street: string;
    voivodeship: string;
}