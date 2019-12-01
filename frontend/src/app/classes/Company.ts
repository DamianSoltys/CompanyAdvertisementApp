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
    putLogoURL?:string;
    getLogoURL?:string;
    logo?:any;
    hasLogoAdded?:boolean;
    socialProfileConnectionDtos?:MediaConnection[];
}

export enum ConnectionStatus {
    connected = "CONNECTED",
    lack_Of_Page = "LACK_OF_PAGE",
    not_Connected = "NOT_CONNECTED",
    expired_Connection = "EXPIRED_CONNECTION",
    lack_Of_Permissions = "LACK_OF_REQUIRED_FACEBOOK_PERMISSIONS"
    
}

export enum MediaTypeEnum {
    FB = "FACEBOOK",
    TWITTER = "TWITTER",
}

export interface MediaConnection {
    connectionStatus: {
        status: ConnectionStatus, 
        profileURL: string
    };
    socialPlatform:MediaTypeEnum;
}

export class Branch {
    actualSelectedLogo?:any;
    address: Address;       
    geoX: string;
    geoY: string;
    name: string;
    branchId?:number;
    companyId?:number;
    logoKey?:string;
    logoURL?:string;
    logoPath?:string;
    putLogoURL?:string;
    getLogoURL?:string;
    logo?:any;
    hasLogoAdded?:boolean;
    category?:string;
}

export class Address {
    apartmentNo: string;
    buildingNo: string;
    city: string;
    street: string;
    voivodeship: string;
}

export interface RecommendationBranch {
    averageRating: number,
    branchId: number,
    category: string,
    companyId: number,
    currentUserRating: number,
    isInsideArea?:number,
    geoX: number,
    geoY: number,
    getLogoURL: string,
    name: string,
    recommendRate?:number,
    logo?:any
}