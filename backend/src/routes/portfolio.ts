import { Router } from 'express';
import { getPortfolioOverview } from '../controllers/portfolioController';

const router = Router();

router.get('/overview', getPortfolioOverview);

export { router as portfolioRouter }; 