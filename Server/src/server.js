const express = require("express");
const ownerRoutes = require("./app/routes/ownerRoutes");
const partnerRoutes = require("./app/routes/partnerRoutes");
const productRoutes = require("./app/routes/productRoutes");
const saleRoutes = require("./app/routes/saleRoutes");
const cors = require("cors");

const app = express();
const port = process.env.PORT || 3000;

app.use(express.json());
app.use(cors());
app.get('/', (req, res) => {
    res.status(200).send('Welcome to Jowo-Jogorogo Administration API');
});
// Setup routes
app.use("/api/owner", ownerRoutes);
app.use("/api/partner", partnerRoutes);
app.use("/api/product", productRoutes);
app.use("/api/sale", saleRoutes);

// Export the app to use with serverless environments like Vercel
module.exports = app;

// This line is only for local development to run the server
if (require.main === module) {
    app.listen(port, () => {
        console.log(`Server running on http://localhost:${port}`);
    });
}
