import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api'
});

export const authAPI = {
    login: (token: string) => api.post('/auth/login', { token }),
    signup: (userData: any) => api.post('/auth/signup', userData)
};

export const portfolioAPI = {
    getPortfolios: () => api.get('/portfolios'),
    createPortfolio: (data: any) => api.post('/portfolios', data)
}; 