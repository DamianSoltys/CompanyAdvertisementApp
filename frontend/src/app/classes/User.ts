export class UserReg {
    name: string;
    email: string;
    password: string;
  }
export class UserLog {
  email: string;
  password: string;
}

export class UserREST {
  companiesIDs: [];
  emailAddress: string;
  loginName: string;
  naturalPersonID: number;
  userID: number;
}

export class PersonalData {
  address:{
    voivodeship: string;
    city: string;
    street: string;
    apartmentNo: string;
    buildingNo: string;
  }
  firstName: string;
  lastName: string;
  phoneNo: string;
}

