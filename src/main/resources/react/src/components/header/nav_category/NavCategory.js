import React from "react";
import "./navCategory.scss";
import {clothes,shoes,toys,transportForChildren,furniture,kidsUpToYear,books,other} from '../../../img/all_images_export/navItems'


const NavCategory = () => {
	return (
		<ul className="navbarBurger-list">
			<div className="wrapper">
				<li className="navbarBurger-element navbarBurger-element-clothes">
					<a href="#">
						<img src={clothes} alt="clothes"/>
						<span>одежда</span>
					</a>
				</li>

				<li className="navbarBurger-element navbarBurger-element-shoes">
					<a href="#">
						<img src={shoes} alt="shoes"/>
						<span>обувь</span>
					</a>
				</li>

				<li className="navbarBurger-element navbarBurger-element-toys">
					<a href="#">
						<img src={toys} alt="toys"/>
						<span>игрушки</span>
					</a>
				</li>

				<li className="navbarBurger-element navbarBurger-element-transportForChildren">
					<a href="#">
						<img src={transportForChildren} alt="transportForChildren"/>
						<span>транспорт для детей</span>
					</a>
				</li>

				<li className="navbarBurger-element navbarBurger-element-furniture">
					<a href="#">
						<img src={furniture} alt="furniture"/>
						<span>детская мебель</span>
					</a>
				</li>

				<li className="navbarBurger-element navbarBurger-element-kidsUpToYear">
					<a href="#">
						<img src={kidsUpToYear} alt="kidsUpToYear"/>
						<span>малыши до года</span>
					</a>
				</li>

				<li className="navbarBurger-element navbarBurger-element-books">
					<a href="#">
						<img src={books} alt="books"/>
						<span>книги</span>
					</a>
				</li>

				<li className="navbarBurger-element navbarBurger-element-other">
					<a href="#">
						<img src={other} alt="other"/>
						<span>другое</span>
					</a>
				</li>
			</div>
		</ul>
	);
};

export default NavCategory;
