export interface OrderRequest {
  tableNumber: number;

  paymentType: string;

  customerName: string;

  email: string;

  udharCode?: string;

  items: any[];
}
