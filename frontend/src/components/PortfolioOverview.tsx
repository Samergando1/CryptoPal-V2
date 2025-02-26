import React from 'react';
import { 
  Paper, 
  Typography, 
  Table, 
  TableBody, 
  TableCell, 
  TableHead, 
  TableRow,
  Box,
  Alert 
} from '@mui/material';

export const PortfolioOverview = () => {
  const [portfolio, setPortfolio] = React.useState<any>(null);
  const [error, setError] = React.useState<string | null>(null);

  React.useEffect(() => {
    fetch('http://localhost:8080/api/portfolio/overview')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch portfolio data');
        return res.json();
      })
      .then(data => setPortfolio(data))
      .catch(err => setError(err.message));
  }, []);

  if (error) return (
    <Alert severity="error" sx={{ m: 2 }}>
      {error} - Please ensure the backend server is running.
    </Alert>
  );

  if (!portfolio) return (
    <Box sx={{ p: 3 }}>
      <Typography>Loading portfolio data...</Typography>
    </Box>
  );

  return (
    <Box sx={{ p: 3 }}>
      <Paper elevation={3} sx={{ p: 2, mb: 2 }}>
        <Typography variant="h4">
          Portfolio Value: ${portfolio.totalValue.toLocaleString()}
        </Typography>
        <Typography color={portfolio['24hChange'] > 0 ? 'success.main' : 'error.main'}>
          24h Change: {portfolio['24hChange']}%
        </Typography>
      </Paper>

      <Paper elevation={3}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Asset</TableCell>
              <TableCell align="right">Price</TableCell>
              <TableCell align="right">Holdings</TableCell>
              <TableCell align="right">Value</TableCell>
              <TableCell align="right">24h</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {portfolio.assets.map((asset: any) => (
              <TableRow key={asset.symbol}>
                <TableCell>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Typography>{asset.name}</Typography>
                    <Typography color="textSecondary" sx={{ ml: 1 }}>
                      {asset.symbol}
                    </Typography>
                  </Box>
                </TableCell>
                <TableCell align="right">${asset.price.toLocaleString()}</TableCell>
                <TableCell align="right">{asset.quantity}</TableCell>
                <TableCell align="right">${asset.value.toLocaleString()}</TableCell>
                <TableCell 
                  align="right"
                  sx={{ color: asset.change24h > 0 ? 'success.main' : 'error.main' }}
                >
                  {asset.change24h}%
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    </Box>
  );
}; 