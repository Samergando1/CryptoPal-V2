## ** Dec 24 - 26 Sprint Planning**

## Setup
- Create a `.env` file to store the global API key.
- Ensure the `.env` file is added to `.gitignore` to keep the key secure.

## JWT Implementation
- Implement JWT token authentication for seamless integration with the frontend when development progresses.

## API Integration (CrypoPal)
- **Tonight**: Research and finalize the API for CryptoPal (e.g., Chainbase API).

---

## Development Steps
1. **API Data Handling**:
    - Ensure the application pulls data correctly from the API.
    - Parse the data in a way that works with the application logic.
    - Save wallet assets to the repository using the associated model (goal: complete this by tonight).

2. **Polling Wallet Address**:
    - Set up HTTP polling to fetch wallet contents every 1 minute.
    - Verify that the polling works as expected by testing updates to wallet contents.

3. **Transaction Endpoint**:
    - Create an endpoint for retrieving transactions.
    - Use the dedicated API endpoint for transactions.

4. **Filtering Transactions**:
    - Write logic to filter transactions based on type (e.g., sent, received).
    - Add functionality to filter coins held in the wallet by type or other attributes.

---

## Reminders
- Ensure all features are implemented with clean, modular, and reusable code.
- Keep testing for each step to validate functionality before moving to the next.

