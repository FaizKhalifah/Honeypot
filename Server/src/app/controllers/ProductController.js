class ProductController {
    constructor(productUseCase) {
        this.productUseCase = productUseCase;
    }

    async addProduct(req, res) {
        try {
            const product = await this.productUseCase.addProduct(req.body);
            res.status(201).json(product);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async updateProduct(req, res) {
        try {
            const { productId } = req.params;
            await this.productUseCase.updateProduct(productId, req.body);
            res.status(200).json({ message: "Product updated successfully" });
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async updateStock(req, res) {
        try {
            const { productId } = req.params;
            const { stock } = req.body;
            await this.productUseCase.updateStock(productId, stock);
            res.status(200).json({ message: "Stock updated successfully" });
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async getAllProducts(req, res) {
        try {
            const products = await this.productUseCase.getAllProducts();
            res.status(200).json(products);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async getProductDetails(req, res) {
        try {
            const { productId } = req.params;
            const product = await this.productUseCase.getProductDetails(
                productId
            );
            if (!product)
                return res.status(404).json({ message: "Product not found" });
            res.status(200).json(product);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async deleteProduct(req, res) {
        try {
            const { productId } = req.params;
            await this.productUseCase.deleteProduct(productId);
            res.status(200).json({ message: "Product deleted successfully" });
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }
}

module.exports = ProductController;
