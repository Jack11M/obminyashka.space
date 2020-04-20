import React from "react";
import "./Footer.scss";
import logoFooter from "../../img/Logo-footer.png";
import {Link} from "react-router-dom";
import BtnGoodBusiness from "../btnGoodBusiness/BtnGoodBusiness.js";

const Footer = () => {
	const timeDate = new Date();
	const yearNow = timeDate.getFullYear();

	return (
		<footer className="HomePageFooter">
			<div className="wrapper">
				<ul className="footer-blocks">
					<li className="footer-list">
						<span className="footer-list__icon icon-phone"></span>
						<a
							href="mailto:&#101;&#109;&#97;&#105;&#108;&#46;&#111;&#98;&#109;&#101;&#110;&#121;&#97;&#115;&#104;&#107;&#97;&#64;&#103;&#109;&#97;&#105;&#108;&#46;&#99;&#111;&#109;">
							&#101;&#109;&#97;&#105;&#108;&#46;&#111;&#98;&#109;&#101;&#110;&#121;&#97;&#115;&#104;&#107;&#97;&#64;&#103;&#109;&#97;&#105;&#108;&#46;&#99;&#111;&#109;
						</a>
						<div className="footer-list-tel">
							<a href="tel:&#43;&#51;&#56;&#48;&#57;&#51;&#49;&#50;&#51;&#52;&#53;&#54;&#55;">
								+3 80 (93) 123 45 67
							</a>
							<a href="tel:&#43;&#51;&#56;&#48;&#57;&#51;&#49;&#50;&#51;&#52;&#53;&#54;&#55;">
								+3 80 (93) 123 45 67
							</a>
						</div>
					</li>
					<li className="footer-list">
						<span className="footer-list__icon icon-question"></span>
						<a href="#">Правила безопасной сделки</a>
						<a href="#">Благотворительные организации</a>
						<a href="#">Часто задаваемые вопросы</a>
					</li>
					<li className="footer-list">
						<span className="footer-list__icon icon-home"></span>
						<Link to="/">
							<img src={logoFooter} alt="Logo"/>
						</Link>
						<BtnGoodBusiness
							text={"Доброе дело"}
							href={"/registration"}
							whatClass={"btn-Help-Children"}
						/>
					</li>
				</ul>
			</div>

			<div className="copyright">
				<span>&copy; Все права защищены </span>
				<span id="dataYear">{yearNow} / Обменяшка</span>
			</div>
		</footer>
	);
};

export default Footer;
