export interface Asset {
  id: string;
  symbol: string;
  name: string;
  quantity: number;
  price: number;
  value: number;
  change24h: number;
  portfolioId: string;
  lastUpdated: Date;
} 