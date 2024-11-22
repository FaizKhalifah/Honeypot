class SaleUseCase {
    constructor(saleRepository) {
        this.saleRepository = saleRepository;
    }

    async recordSale(partner_id, salesData) {
        const results = {
            success: [],
            errors: [],
        };

        // Process each sale item
        for (const { product_id, quantity, sale_date } of salesData) {
            try {
                // Fetch the PartnerStock with associated Product to get `price_per_unit`
                const partnerStock = await this.saleRepository.getPartnerStock(
                    partner_id,
                    product_id
                );

                if (!partnerStock || !partnerStock.Product) {
                    results.errors.push({
                        product_id,
                        message: "Product not found in partner stock",
                    });
                    continue;
                }

                // Check if there's enough stock
                if (partnerStock.stock < quantity) {
                    results.errors.push({
                        product_id,
                        message: "Insufficient stock in partner",
                        available_stock: partnerStock.stock,
                    });
                    continue;
                }

                // Calculate total price based on Product's price_per_unit
                const totalPrice =
                    partnerStock.Product.price_per_unit * quantity;

                // Record sale
                const sale = await this.saleRepository.createSale({
                    partner_id,
                    product_id,
                    quantity,
                    sale_date,
                    total_price: totalPrice,
                });

                // Update stock in PartnerStock
                await this.saleRepository.updatePartnerStock(
                    partner_id,
                    product_id,
                    partnerStock.stock - quantity
                );

                // Add sale to success results
                results.success.push(sale);
            } catch (error) {
                results.errors.push({
                    product_id,
                    message: `Error processing sale for product ${product_id}: ${error.message}`,
                });
            }
        }

        // If all products failed, throw an error with details
        if (results.success.length === 0 && results.errors.length > 0) {
            throw new Error(
                "All sales failed: " + JSON.stringify(results.errors)
            );
        }

        return results;
    }

    async getMonthlyReport(partnerId, month, year) {
        const sales = await this.saleRepository.findSalesByMonth(
            partnerId,
            month,
            year
        );

        // Calculate total per product and overall
        const report = sales.reduce(
            (acc, sale) => {
                const { product_id, quantity, total_price } = sale;
                if (!acc.products[product_id]) {
                    acc.products[product_id] = {
                        product_id,
                        quantity: 0,
                        total_price: 0,
                    };
                }
                acc.products[product_id].quantity += quantity;
                acc.products[product_id].total_price += total_price;

                acc.total_quantity += quantity;
                acc.total_revenue += total_price;

                return acc;
            },
            { products: {}, total_quantity: 0, total_revenue: 0 }
        );

        return report;
    }
    async getTotalSalesData() {
        const { salesData, overallTotal } =
            await this.saleRepository.getTotalSalesData();

        const formattedData = {
            partnerSales: {},
            productSales: {},
            overallTotal: {
                total_quantity: overallTotal.dataValues.total_quantity,
                total_revenue: overallTotal.dataValues.total_revenue,
            },
        };

        salesData.forEach((sale) => {
            const { partner_id, product_id, total_quantity, total_revenue } =
                sale.dataValues;
            const partnerName = sale.Partner.name;
            const productName = sale.Product.name;

            // Aggregate by partner
            if (!formattedData.partnerSales[partnerName]) {
                formattedData.partnerSales[partnerName] = {
                    total_quantity: 0,
                    total_revenue: 0,
                    products: {},
                };
            }
            formattedData.partnerSales[partnerName].total_quantity +=
                total_quantity;
            formattedData.partnerSales[partnerName].total_revenue +=
                total_revenue;

            // Aggregate by product under each partner
            if (
                !formattedData.partnerSales[partnerName].products[productName]
            ) {
                formattedData.partnerSales[partnerName].products[productName] =
                    {
                        total_quantity: 0,
                        total_revenue: 0,
                    };
            }
            formattedData.partnerSales[partnerName].products[
                productName
            ].total_quantity += total_quantity;
            formattedData.partnerSales[partnerName].products[
                productName
            ].total_revenue += total_revenue;

            // Aggregate total sales by product across all partners
            if (!formattedData.productSales[productName]) {
                formattedData.productSales[productName] = {
                    total_quantity: 0,
                    total_revenue: 0,
                };
            }
            formattedData.productSales[productName].total_quantity +=
                total_quantity;
            formattedData.productSales[productName].total_revenue +=
                total_revenue;
        });

        return formattedData;
    }
}

module.exports = SaleUseCase;
