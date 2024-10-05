import React from "react";
import { Button, Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { Link } from "react-router-dom";

function NavBar() {
	return (
		<Navbar collapseOnSelect expand="md" bg="dark" data-bs-theme="dark">
			<Container>
				<Navbar.Brand>
					<Link to={"/"} className="nav-link">
						KoiTripCo.
					</Link>
				</Navbar.Brand>
				<Navbar.Toggle aria-controls="navbar-nav" />
				<Navbar.Collapse id="navbar-nav">
					<Nav className="me-auto navbar-wrapper">
						<Link to={"/"} className="nav-link">
							Home
						</Link>
						<Link to={"/About"} className="nav-link">
							About
						</Link>
						<Link to={"/Guidelines"} className="nav-link">
							Guidelines
						</Link>
						<NavDropdown title="Services" id="collapsible-nav-dropdown">
							<NavDropdown.Item>
								<Link to={"/KoiList"} className="nav-link">
									Koi List
								</Link>
							</NavDropdown.Item>
							<NavDropdown.Item>
								<Link to={"/FarmList"} className="nav-link">
									Farm Site
								</Link>
							</NavDropdown.Item>
							<NavDropdown.Item>
								<Link to={"/TourList"} className="nav-link">
									Farm Tour
								</Link>
							</NavDropdown.Item>
						</NavDropdown>
						{localStorage.getItem("user-info") ? (
							//After login, it will show this.
							<>You Login</>
						) : (
							//Not login will show the login link
							<>
								<Link to={"/Login"} className="nav-link">
									Login
								</Link>
								<Link to={"/Register"} className="nav-link">
									Register
								</Link>
							</>
						)}
					</Nav>
				</Navbar.Collapse>
			</Container>
		</Navbar>
	);
}

export default NavBar;
