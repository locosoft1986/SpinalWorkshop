package workshop.uart

import spinal.core._
import spinal.lib._

case class UartRxGenerics( preSamplingSize: Int = 1,
                           samplingSize: Int = 5,
                           postSamplingSize: Int = 2){
  val rxdSamplePerBit = preSamplingSize + samplingSize + postSamplingSize
  assert(isPow2(rxdSamplePerBit))
  if ((samplingSize % 2) == 0)
    SpinalWarning(s"It's not nice to have a even samplingSize value at ${ScalaLocated.short} (because of the majority vote)")
}

object UartCtrlRxState extends SpinalEnum {
  val IDLE, START, DATA, STOP = newElement()
}

class UartCtrlRx(generics : UartRxGenerics) extends Component{
  import generics._
  val io = new Bundle{
    val rxd          = in Bool
    val samplingTick = in Bool
    val read         = master Flow(Bits(8 bits))
  }

  // Implement the rxd sampling with a majority vote over samplingSize bits
  // Provide a new sampler.value each time sampler.tick is high
  val sampler = new Area {

  }

  // Provide a bitTimer.tick each rxSamplePerBit
  // reset() can be called to recenter the counter over a start bit.
  val bitTimer = new Area {

  }

  // Provide bitCounter.value that count up each bitTimer.tick, Used by the state machine to count data bits and stop bits
  // reset() can be called to reset it to zero
  val bitCounter = new Area {

  }
  io.read.assignDontCare()
  // Statemachine that use all precedent area
  val stateMachine = new Area {
    import UartCtrlRxState._

    val state = RegInit(IDLE)
    switch(state) {
      is(IDLE) {

      }
      is(START) {

      }
      is(DATA) {

      }
      is(STOP) {

      }
    }
  }
}




object UartCtrlRx{
  def main(args: Array[String]) {
    SpinalVhdl(new UartCtrlRx(UartRxGenerics(1,5,2)))
    assert(doCmd(s"ghdl -a --ieee=synopsys UartCtrlRx.vhd UartCtrlRx_tb.vhd") == 0,"GHDL analysis fail")
    assert(doCmd(s"ghdl -e --ieee=synopsys UartCtrlRx_tb"                   ) == 0,"GHDL elaboration fail")
    assert(doCmd(s"ghdl -r --ieee=synopsys UartCtrlRx_tb --vcd=wave.vcd"    ) == 0,"GHDL simulation fail")
  }

  def doCmd(cmd : String) : Int = {
    import scala.sys.process._
    println(cmd)
    cmd !
  }
}
