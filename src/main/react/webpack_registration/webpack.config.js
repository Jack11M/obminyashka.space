const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const TerserWebpackPlugin = require("terser-webpack-plugin");
const OptimizeCssAssetsWebpackPlugin = require("optimize-css-assets-webpack-plugin");

const optimization = () => {
  const config = {
    splitChunks: {
      chunks: "all",

    }
  };
  if (isProd) {
    config.minimizer = [
      new OptimizeCssAssetsWebpackPlugin(),
      new TerserWebpackPlugin()
    ];
  }
  return config;
};

const cssLoaders = extra => {
  const loaders = [
    {
      loader: MiniCssExtractPlugin.loader,
      options: {
        hmr: isDev,
        reloadAll: true
      }
    },
    "css-loader"
  ];
  extra && loaders.push(extra);

  return loaders;
};

const babelOptions = preset => {
  const opts = {
    presets: ["@babel/preset-env"],
    plugins: ["@babel/plugin-proposal-class-properties"]
  };
  if (preset) {
    opts.presets.push(preset);
  }
  return opts;
};

const isDev = process.env.NODE_ENV === "development";
const isProd = !isDev;

module.exports = {
  context: path.resolve(__dirname, "web_pack"),
  entry: {
    registration: ["@babel/polyfill", "./index.js"]
  },
  output: {
    path: path.resolve(__dirname, "../../resources/static"),
    filename: "js/register/[name].js"
  },
  devServer: {
    openPage: "html/registration.html",
    hot: isDev,
    overlay: {
      warnings: true,
      errors: true
    },
    port: 3999
  },
  // resolve: {
  //   extensions: [".js", ".css", ".scss", "jpeg", "jpg", "png", ".svg"],
  //   alias: {
  //     "@modules": path.resolve(__dirname, "web_pack/components"),
  //     "@": path.resolve(__dirname, "wep_pack")
  //   }
  // },
  optimization: optimization(),

  devtool: isDev ? "source-map" : "",
  module: {
    rules: [
      {
        test: /\.css$/i,
        use: cssLoaders()
      },
      {
        test: /\.s[ac]ss$/i,
        use: cssLoaders("sass-loader")
      },
      {
        test: /\.(png|jpe?g|svg|gif)$/i,
        use: [
          {
            loader: "file-loader",
            options: {
              name: "[name]-[sha1:hash:7].[ext]",
              outputPath: "img",
            }
          }
        ]
      },

      // {
      //   test: /\.js$/,
      //   exclude: /node_modules/,
      //   loader: {
      //     loader: "babel-loader",
      //     options: babelOptions()
      //   }
      // },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: {
          loader: "babel-loader",
          options: babelOptions("@babel/preset-react")
        }
      },
      {
        test: /\.ts$/,
        exclude: /node_modules/,
        loader: {
          loader: "babel-loader",
          options: babelOptions("@babel/preset-typescript")
        }
      }
    ]
  },

  plugins: [
    new HtmlWebpackPlugin({
      template: "html/registration.html",
      filename: "html/registration.html",
      // minify: {
      //   collapseWhitespace: isProd
      // }
    }),
    // new CleanWebpackPlugin(),
    // new CopyWebpackPlugin([
    //   {
    //     from: path.resolve(__dirname, "web_pack/img"),
    //     to: path.resolve(__dirname, "static/img")
    //   }
    // ]),
    new MiniCssExtractPlugin({
      filename: "css/[name].css"
    }),

  ]
};
