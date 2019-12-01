import { Address } from './Company';

export interface SectionData {
    address:Address;
    name:string;
    companyId?:number;
    id?:number;
    type:string;
    category:string;
    logo?:any;
    logoKey?:string;
    putLogoURL?:string;
    getLogoURL?:string;
}

export interface SearchResponse {
    branchesNumber: number;
    companiesNumber: number;
    result: SectionData[];
}

export interface CommentResponse {
    content:Array<any>;
    empty: boolean;
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    pageable: Pagable;
    size: number;
    sort: Sort;
    totalElements: number;
    totalPages: number;
}

interface Sort {
    sorted?: boolean; 
    unsorted?: boolean; 
    empty?: boolean;

}

interface Pagable {
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    sort: Sort;
    unpaged: boolean;
}