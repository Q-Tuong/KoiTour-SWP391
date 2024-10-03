import React from "react";
import { Button, Container, Nav, Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";

function NavBar() {
	return (
		<Navbar collapseOnSelect expand="md" bg="dark" data-bs-theme="dark">
			<Container>
				<Navbar.Brand>
					<Link to={"/"}>LAB1</Link>
				</Navbar.Brand>
				<Navbar.Toggle aria-controls="navbar-nav" />
				<Navbar.Collapse id="navbar-nav">
					<Nav className="me-auto">
						<Nav.Link>
							<Link to={"/"}>Home</Link>
						</Nav.Link>
						<Nav.Link>
							<Link to={"/About"}>About</Link>
						</Nav.Link>
						<Nav.Link>
							<Link to={"/Original"}>Original</Link>
						</Nav.Link>
						<Nav.Link>
							<Link to={"/Contact"}>Contact</Link>
						</Nav.Link>
					</Nav>
					<Nav>
						<Button variant="secondary">Change mode</Button>
					</Nav>
				</Navbar.Collapse>
			</Container>
		</Navbar>
	);
}

export default NavBar;
