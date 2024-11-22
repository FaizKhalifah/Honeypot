const SaleController = require("../../app/controllers/SaleController");
const SaleRepository = require("../../domain/repositories/SaleRepository");
const SaleUseCase = require("../../domain/use-cases/SaleUseCase");
const {
    Sale,
    Partner,
    Product,
} = require("../../infrastructure/database/models");

const saleRepository = new SaleRepository(Sale, Partner, Product);
const saleUseCase = new SaleUseCase(saleRepository);
const saleController = new SaleController(saleUseCase);

const saleHttpHandler = {
    async recordSale(req, res) {
        return saleController.recordSale(req, res);
    },

    async getMonthlyReport(req, res) {
        return saleController.getMonthlyReport(req, res);
    },

    async getTotalSalesReport(req, res) {
        return saleController.getTotalSalesReport(req, res);
    },
};

module.exports = saleHttpHandler;
