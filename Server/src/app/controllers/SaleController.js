class SaleController {
    constructor(saleUseCase) {
        this.saleUseCase = saleUseCase;
    }

    async recordSale(req, res) {
        try {
            const { partnerId } = req.params;
            const salesData = req.body;

            const result = await this.saleUseCase.recordSale(
                partnerId,
                salesData
            );
            res.status(200).json(result);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async getMonthlyReport(req, res) {
        try {
            const { partnerId } = req.params;
            const { month, year } = req.query;
            const report = await this.saleUseCase.getMonthlyReport(
                partnerId,
                parseInt(month),
                parseInt(year)
            );
            res.status(200).json(report);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }
    async getTotalSalesReport(req, res) {
        try {
            const result = await this.saleUseCase.getTotalSalesData();
            res.status(200).json(result);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }
}

module.exports = SaleController;
