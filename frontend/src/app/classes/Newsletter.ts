export interface PromotionItem {
    companyId?:number,
    destinations?:Destination[],
    htmlContent?:string,
    nonHtmlContent?:string,
    numberOfPhotos?:number,
    photoURLs?:string[],
    promotionItemType?:PromotionType,
    sendingStrategy?:SendingStrategy,
    sendingStatus?:string,
    startTime?:string,
    title?:string,
    uuid?:string
}

export enum Destination {
    FB='FB',
    TWITTER='TWITTER',
    NEWSLETTER='NEWSLETTER'
}

export enum PromotionType {
    PROMOTION='PROMOTION',
    PRODUCT='PRODUCT',
    INFORMATION='INFORMATION'
}

export enum SendingStrategy {
    AT_CREATION='AT_CREATION',
    DELAYED='DELAYED',
    AT_WILL='AT_WILL'
}