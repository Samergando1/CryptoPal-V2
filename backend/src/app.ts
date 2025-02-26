import express from 'express';
import cors from 'cors';
import { portfolioRouter } from './routes/portfolio';
import { authRouter } from './routes/auth';
import { errorHandler } from './middleware/errorHandler';
import { PrismaClient } from '@prisma/client';

const app = express();
const prisma = new PrismaClient();

app.use(cors());
app.use(express.json());

app.use('/api/portfolio', portfolioRouter);
app.use('/api/auth', authRouter);

app.use(errorHandler);

export { app, prisma }; 