import { lot1 , lot10 , lot2 , lot3 , lot4 , lot5 , lot6 } from '../../img/all_images_export/cards';

export const ProductDB = {
	photo : [ lot1 , lot2 , lot3 , lot4 , lot5 , lot6 ] ,
	title : 'some text' ,
	description : [ 'some text' , 'some text some text some text some text' ] ,
	owner : {
		avatar : lot10 ,
		name : 'some name'
	} ,
	postData : {
		date : 'some date' ,
		city : 'some town' ,
		telephone : 'some telephone'
	},
	productData : {
		nameItem: 'some name item',
		forChange : ['some', 'text', 'may be', 'no'],
		specification: {
			sex: 'male',
			season : 'winter',
			stateProduct: 'nope',
			size_age: 'epoa'
		}
	}
};
