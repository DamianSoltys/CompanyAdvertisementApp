export interface PromotionItem {
    companyId?: number,
    destinations?: Destination[],
    htmlContent?: string,
    nonHtmlContent?: string,
    numberOfPhotos?: number,
    photoURLs?: string[],
    promotionItemType?: PromotionType,
    sendingStrategy?: SendingStrategy,
    plannedSendingTime?: string,
    emailTitle?: string,
    name?: string,
    uuid?: string,
};

export interface PromotionItemResponse {
    name?: string,
    addedTime?: any,
    plannedSendingTime?: any,
    promotionItemUUID?: string,
    sendingStatus?: SendingStatus[],
};

export interface SendingStatus {
    destination: string,
    detail: string,
    failedAt: any,
    sendingStatus: string,
    plannedSendingAt: any,
    sendAt: any,
};

export enum SendStatus {
    SENT = 'sent',
    WAITING = 'waiting_for_action',
    DELAYED = 'delayed',
};

export enum SendStatusPL {
    SENT = 'Wysłany',
    WAITING = 'Oczekiwanie na wysłanie',
    DELAYED = 'Wysyłka opóźniona',
};

export enum Destination {
    FB = 'FB',
    TWITTER = 'TWITTER',
    NEWSLETTER = 'NEWSLETTER',
};

export enum PromotionType {
    PROMOTION = 'PROMOTION',
    PRODUCT = 'PRODUCT',
    INFORMATION = 'INFORMATION',
};

export enum SendingStrategy {
    AT_CREATION = 'AT_CREATION',
    DELAYED = 'DELAYED',
    AT_WILL = 'AT_WILL',
};