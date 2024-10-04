import React from "react";
import { Button, Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { Link } from "react-router-dom";

function NavBar() {
	return (
		<Navbar collapseOnSelect expand="md" bg="dark" data-bs-theme="dark">
			<Container>
				<Navbar.Brand>
					<Link to={"/"}>KoiTripCo.</Link>
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
							<Link to={"/Guidelines"}>Guidelines</Link>
						</Nav.Link>
						<NavDropdown title="Services" id="collapsible-nav-dropdown">
							<NavDropdown.Item>
								<Link to={"/KoiList"}>Koi List</Link>
							</NavDropdown.Item>
							<NavDropdown.Item>
								<Link to={"/FarmList"}>Farm Site</Link>
							</NavDropdown.Item>
							<NavDropdown.Item>
								<Link to={"/TourList"}>Farm Tour</Link>
							</NavDropdown.Item>
						</NavDropdown>
						<Nav.Link>
							<Link to={"/Login"}>Login</Link>
						</Nav.Link>
					</Nav>
				</Navbar.Collapse>
			</Container>
		</Navbar>
	);
}

export default NavBar;
