import { Address } from './Company';

export interface UserReg {
  name?: string,
  email?: string,
  password?: string,
}
export interface UserLog {
  email?: string,
  password?: string,
}

export interface UserREST {
  companiesIDs: any[],
  emailAddress: string,
  loginName: string,
  naturalPersonID: number,
  userID: number,
}

export interface PersonalData {
  address: Address,
  firstName: string,
  lastName: string,
  phoneNo: string,
}

