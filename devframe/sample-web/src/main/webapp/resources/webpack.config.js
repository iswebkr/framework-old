const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');
const JspWebpackPlugin = require('jsp-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin');
const webpack = require('webpack'); // to access built-in pluins
const path = require('path');
// const HtmlWebpackPlugin = require('html-webpack-plugin');

// environment vraibles : https://webpack.js.org/guides/environment-variables/
module.exports = (env, argv) => {

	console.log('Start Mode : ', argv.mode);

	return {
		/**
		 * mode : development, production, none
		 * reference : https://webpack.js.org/configuration/mode/
		 */
		mode: 'development',
		/**
		 * webpack source map settings
		 * reference : https://webpack.js.org/configuration/devtool/#devtool
		 */
		devtool: 'inline-source-map',
		/**
		 * entry source file 설정
		 * reference : https://webpack.js.org/configuration/entry-context/#entry
		 * resources/src/index.js 의 소스 파일을 읽어 output 파일을 생성
		 * entry : {
		 *     main: './src/index.js',
		 *     login: './src/login.js'
		 * }
		 * 와 같은 형태로 여러개의 엔트리 포인트를 분리하여 사용할 수 있음
		 * 이런 경우 싱글페이지 어플리케이션이 아닌 특정 페이지로 진입했을 때 서버에서 해당 정보를 내려주는 형태의 멀티 페이지 어플리케이션에 적합
		 */
		// entry: ['@babel/polyfill', path.resolve(__dirname, 'resources/src/js/index.js')],
		entry: {
			sample: {
				import: path.resolve(__dirname, 'src/js/app.main.js'),
			},
		},
		/**
		 * 컴파일 + 번들링되어진 js 파일이 저장될 경로와 파일 이름 지정
		 * resources/dist/bundle.js 형태로 파일을 생성 path.resolve node api 를 사용하여 경로 조합
		 * reference : https://webpack.js.org/configuration/output/
		 */
		output: {
			filename: (pathData, assetInfo) => {
				return '[name].bundle.js';
			},
			sourceMapFilename: 'bundle.source.map',
			path: path.resolve(__dirname, 'dist'),
			chunkFilename: (pathData, assetInfo) => {
				return '[name].bundle.js'
			},
			crossOriginLoading: 'anonymous',
			clean: true,
		},
		/**
		 * dev server 설정
		 * reference : https://webpack.js.org/configuration/dev-server/
		 */
		devServer: {
			port: 9000,
			contentBase: '.',
			proxy: {
				"/city/*": {
					target: 'http://localhost:9090'
				}
			},
			/**
			proxy: {
				'/' : {
					target: 'http://localhost:9090'
				}
			},
			**/
			compress: true,
			inline: true,
			https: false,
			hot: true
		},
		/**
		 * module 설정
		 * reference : https://webpack.js.org/configuration/module/
		 */
		module: {
			rules: [
				// babel-loader for .js
				{
					test: /\.m?js$/,
					include: path.join(__dirname, 'src/js'),
					exclude: /(node_modules|bower_components)/,
					use: {
						loader: 'babel-loader',
						options: {
							presets: ['@babel/preset-env'],
							plugins: ['@babel/plugin-proposal-object-rest-spread', '@babel/plugin-proposal-class-properties']
						}
					}
				},
				// css-loader for .css
				{
					test: /\.css$/i,
					use: [MiniCssExtractPlugin.loader, {
						loader: 'css-loader',
						options: {
							sourceMap: true
						}
					}],
				},
				// css-loader
				{
					test: /\.scss$/,
					use: [MiniCssExtractPlugin.loader, 'style-loader', 'css-loader', 'scss-loader']
				}
			]
		},
		/**
		 * plugin 설정
		 * reference : https://webpack.js.org/configuration/plugins/
		 */
		plugins: [
			new webpack.EnvironmentPlugin({
				MODE_ENV: argv.mode,
				DEBUG: false
			}),
			new webpack.ProgressPlugin(),
			new CleanWebpackPlugin(),
			new MiniCssExtractPlugin({
				filename: '[name].css',
				chunkFilename: '[name].css',
			}),
			new webpack.ProvidePlugin({
				$: 'jquery',
				jQuery: 'jquery'
			}),
			new webpack.LoaderOptionsPlugin({
				minimize: true
			}),
			new JspWebpackPlugin({
				template: path.resolve(__dirname, 'src/jsp/default.jsp'),
				filename: '../templates/default.jsp',
			}),
		],
		/**
		 * optimization 설정
		 * reference : https://webpack.js.org/configuration/optimization/
		 */
		optimization: {
			minimize: true,
			minimizer: [
				new CssMinimizerPlugin(),
				new TerserPlugin({
					test: /\.js(\?.*)?$/i,
					parallel: true,
				})
			],
			chunkIds: 'named',
			moduleIds: 'named',
			runtimeChunk: 'single',
			splitChunks: {
				chunks: 'all',
				cacheGroups: {
					vendors: {
						test: /(node_modules)/,
						name: 'vendors',
					}
				}
			}
		}
	}
}