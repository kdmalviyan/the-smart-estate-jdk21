import { Budget } from "../lead-management/lead-management.component";

export class FilterConstants {
   
   public static  page: any = {
        count: 0, // total count of items
        offset: 0, // Page number
        limit: 20, // number of items per page
        orderBy: 'createdAt',
        orderDir: 'DESC'
    };

    public static   budget: Budget = {
        startAmount: 0,
        startUnit: 'LAC',
        endAmount: 800,
        endUnit: 'CRORE'
      }
}