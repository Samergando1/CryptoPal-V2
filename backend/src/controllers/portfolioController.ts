import { Request, Response } from 'express';
import { prisma } from '../app';

export const getPortfolioOverview = async (req: Request, res: Response) => {
  try {
    // Mock data for now
    const portfolio = {
      totalValue: 15234.50,
      change24h: 2.5,
      assets: [
        {
          symbol: "BTC",
          name: "Bitcoin",
          quantity: 0.5,
          value: 8500.00,
          price: 17000.00,
          change24h: 1.2
        },
        {
          symbol: "ETH",
          name: "Ethereum",
          quantity: 4.2,
          value: 6734.50,
          price: 1603.45,
          change24h: 3.4
        }
      ]
    };

    res.json(portfolio);
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch portfolio data' });
  }
}; 