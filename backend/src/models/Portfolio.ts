import { Asset } from './Asset';
import { User } from './User';

export interface Portfolio {
  id: string;
  userId: string;
  name: string;
  totalValue: number;
  change24h: number;
  assets: Asset[];
  createdAt: Date;
  updatedAt: Date;
  user: User;
} 