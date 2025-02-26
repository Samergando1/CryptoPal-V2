using HTTP
using JSON
using Pkg


function fetch_data()
    # Define the API endpoint
    base_url = "https://api.chainbase.online/v1/account/tokens"
    api_key = "2qgY9Qf3IzBp1i2UEvEsI9LJAj0"  # Replace with your actual API key

    # Define the required query parameters
    query_params = Dict(
        "chain_id" => "1",  # Ethereum's chain ID
        "address" => "0x1C727a55eA3c11B0ab7D3a361Fe0F3C47cE6de5d",  # Replace with the target wallet address
        "limit" => "10",  # Optional: number of results per page (default: 10)
        "page" => "1"     # Optional: page number to fetch (default: 1)
    )

    # Construct the full URL with query parameters
    query_string = "?" * join(["$k=$v" for (k, v) in query_params], "&")
    url = base_url * query_string

    # Set the request headers
    headers = Dict("x-api-key" => api_key)

    # Debugging: Print the request details
    println("Request URL: ", url)
    println("Request Headers: ", headers)

    # Make the GET request
    response = HTTP.get(url, headers)

    if response.status == 200
        data = JSON.parse(String(response.body))
        println("Data fetched successfully!")

        # Save the response to a JSON file
        output_file = "response.json"
        open(output_file, "w") do file
            JSON.print(file, data)
        end
        println("Response saved to: ", output_file)
    else
        println("Error: HTTP ", response.status)
        println("Response Body: ", String(response.body))
    end
end

# Call the function to fetch the data
fetch_data()
